package com.merlin.HOUSE.HUNTING.SYSTEM.Media;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MediaMapper {

    public MediaResponseDto toMediaResponseDto(Media media) {
        return new MediaResponseDto(media.getId(), media.getUrl(), media.getCaption());
    }
}
