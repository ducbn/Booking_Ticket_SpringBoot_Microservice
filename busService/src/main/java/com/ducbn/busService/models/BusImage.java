package com.ducbn.busService.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bus_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BusImage {
    public static final int MAXIMUM_IMAGES_PER_BUS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    @JsonBackReference
    private Bus bus;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
