package com.ducbn.busService.controllers;

import com.ducbn.busService.dtos.BusDTO;
import com.ducbn.busService.models.*;
import com.ducbn.busService.responses.BusListResponse;
import com.ducbn.busService.responses.BusResponse;
import com.ducbn.busService.services.BusService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {
    private final BusService busService;

    // tạo mới 1 bus
    @PostMapping("")
    public ResponseEntity<?> createBus(@Valid @RequestBody BusDTO busDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Bus newBus = busService.createBus(busDTO);
            return ResponseEntity.ok(newBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // thêm ảnh cho bus mới
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImageBus(
            @PathVariable("id") Long busId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            if(files.size() > BusImage.MAXIMUM_IMAGES_PER_BUS) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }

            for (MultipartFile file : files) {
                if (!isImageFile(file)) {
                    return ResponseEntity.badRequest()
                            .body("File " + file.getOriginalFilename() + "File must be an image");
                }
            }

            List<String> imageUrls = busService.createBusImages(busId, files);
            return ResponseEntity.ok(imageUrls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private boolean isImageFile(MultipartFile file) {
        List<String> allowedContentTypes = List.of(
                "image/jpeg",
                "image/png",
                "image/webp"
        );
        String contentType = file.getContentType();
        return contentType != null && allowedContentTypes.contains(contentType);
    }

    @GetMapping("")
    public ResponseEntity<BusListResponse> getProducts(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10")  int limit
    ){
        //Tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending());

        Page<BusResponse> busPages = busService.getAllBuses(pageRequest);

        //lấy tổng số trang
        int totalPages = busPages.getTotalPages();
        List<BusResponse> bus = busPages.getContent();

        return ResponseEntity.ok(BusListResponse.builder()
                .buses(bus)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBusById(@PathVariable("id") Long id) {
        try {
            Bus bus = busService.getBusById(id);
            return ResponseEntity.ok(BusResponse.fromBus(bus));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBus(
            @PathVariable("id") Long id,
            @Valid @RequestBody BusDTO busDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Bus updatedBus = busService.updateBus(id, busDTO);
            return ResponseEntity.ok(updatedBus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable("id") Long id) {
        try {
            busService.deleteBus(id);
            return ResponseEntity.ok(String.format("Bus id = %s deleted", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generateFakeBus")
    public ResponseEntity<String> generateFakeBus() {
        Faker faker = new Faker(new Locale("vi"));
        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            String busName = faker.bothify("##B-#####");

            // Tránh trùng tên xe
            if (busService.existsByName(busName)) {
                continue;
            }

            // Giờ khởi hành từ 6h đến 15h, giờ đến sau đó từ 1–3 tiếng
            int departureHour = random.nextInt(10) + 6;
            int arrivalHour = departureHour + random.nextInt(3) + 1;

            // Random ID giả định có sẵn
            Long routeId = (long) (random.nextInt(4) + 1);
            Long companyId = (long) (random.nextInt(3) + 1);
            Long busTypeId = (long) (random.nextInt(3) + 1);

            // Giá từ 50k đến 300k
            BigDecimal price = BigDecimal.valueOf((random.nextInt(26) + 5) * 10000);

            BusDTO busDTO = BusDTO.builder()
                    .name(busName)
                    .departureTime(LocalTime.of(departureHour, 0))
                    .arrivalTime(LocalTime.of(arrivalHour, 0))
                    .price(price)
                    .routeId(routeId)
                    .companyId(companyId)
                    .busTypeId(busTypeId)
                    .build();
            try {
                busService.createBus(busDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Product generated successfully");
    }

}
