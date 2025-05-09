package com.ducbn.busService.services;

import com.ducbn.busService.dtos.BusDTO;
import com.ducbn.busService.exceptions.InvalidParamException;
import com.ducbn.busService.models.*;
import com.ducbn.busService.repositorys.BusImageRepository;
import com.ducbn.busService.repositorys.BusRepository;
import com.ducbn.busService.repositories.BusTypeRepository;
import com.ducbn.busService.repositorys.CompanyRepository;
import com.ducbn.busService.repositories.RouteRepository;
import com.ducbn.busService.repositorys.SeatRepository;
import com.ducbn.busService.responses.BusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusService implements IBusService {

    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final CompanyRepository companyRepository;
    private final BusTypeRepository busTypeRepository;
    private final S3Client s3Client;
    private final BusImageRepository busImageRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public Bus createBus(BusDTO busDTO) {
        Route route = routeRepository
                .findById(busDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + busDTO.getRouteId()));

        Company company = companyRepository
                .findById(busDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + busDTO.getCompanyId()));

        BusType busType = busTypeRepository
                .findById(busDTO.getBusTypeId())
                .orElseThrow(() -> new RuntimeException("BusType not found with id: " + busDTO.getBusTypeId()));

        // kiểm tra giờ đi < giờ đến
        LocalTime departureTime = busDTO.getDepartureTime();
        LocalTime arrivalTime = busDTO.getArrivalTime();
        if (arrivalTime.isBefore(departureTime)) {
            throw new RuntimeException("Arrival time must be after departure time");
        }

        // Tạo đối tượng Bus
        Bus bus = Bus.builder()
                .name(busDTO.getName())
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .price(busDTO.getPrice())
                .route(route)
                .company(company)
                .busType(busType)
                //.thumbnail(busDTO.getThumbnail())
                .build();

        // Lưu bus trước để có ID
        bus = busRepository.save(bus);

        // Tạo danh sách ghế dựa theo seatCapacity của BusType
        int seatCount = Math.toIntExact(busType.getSeatCapacity());
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= seatCount; i++) {
            Seat seat = Seat.builder()
                    .bus(bus)
                    .seatNumber(String.format("A%02d", i)) // A01, A02, ...
                    .isAvailable(true)
                    .build();
            seats.add(seat);
        }

        // Lưu tất cả seats
        seatRepository.saveAll(seats);

        return bus;
    }


    @Override
    @Transactional
    public List<String> createBusImages(Long busId, List<MultipartFile> files) throws Exception {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + busId));

        //khong cho insert qua 5 anh cho 1 san pham
        int size = busImageRepository.findByBusId(busId).size();
        if(size >= BusImage.MAXIMUM_IMAGES_PER_BUS) {
            throw new InvalidParamException("Number of image must be <= " + BusImage.MAXIMUM_IMAGES_PER_BUS);
        }

        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket("bus-app-images")
                    .key("buses/" + filename)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = "https://" + "bus-app-images" + ".s3.amazonaws.com/buses/" + filename;

            // Lưu vào DB
            BusImage image = BusImage.builder()
                    .bus(bus)
                    .imageUrl(fileUrl)
                    .build();
            busImageRepository.save(image);

            uploadedUrls.add(fileUrl);
        }

        return uploadedUrls;
    }


    @Override
    public Page<BusResponse> getAllBuses(PageRequest pageRequest) {
        Page<Bus> busPage = busRepository.findAll(pageRequest);
        return busPage.map(BusResponse::fromBus);
    }

    @Override
    public boolean existsByName(String busName) {
        return busRepository.existsByName(busName);
    }

    @Override
    public Bus getBusById(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));
    }

    @Override
    public Bus updateBus(Long id, BusDTO busDTO) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));

        Route route = routeRepository.findById(busDTO.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + busDTO.getRouteId()));

        Company company = companyRepository.findById(busDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + busDTO.getCompanyId()));

        BusType busType = busTypeRepository.findById(busDTO.getBusTypeId())
                .orElseThrow(() -> new RuntimeException("BusType not found with id: " + busDTO.getBusTypeId()));

        bus.setName(busDTO.getName());
        bus.setRoute(route);
        bus.setCompany(company);
        bus.setBusType(busType);
        bus.setDepartureTime(busDTO.getDepartureTime());
        bus.setArrivalTime(busDTO.getArrivalTime());
        bus.setPrice(busDTO.getPrice());

        return busRepository.save(bus);
    }

    @Override
    public void deleteBus(Long id) {
        busRepository.deleteById(id);
    }
}
