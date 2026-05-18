package com.merlin.HOUSE.HUNTING.SYSTEM.Media;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MediaMapper {

    public Media toMedia(MediaDto mediaDto) {
        Media media = new Media();

        media.setUrl(mediaDto.url());
        media.setCaption(mediaDto.caption());
        media.setMediaType(mediaDto.mediaType());
        media.setCreatedAt(LocalDateTime.now());

        return media;
    }

    public MediaResponseDto toMediaResponseDto(Media media) {
        return new MediaResponseDto(media.getId(), media.getUrl(), media.getCaption());
    }
}
