package com.merlin.HOUSE.HUNTING.SYSTEM.Media;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    public MediaResponseDto addMedia(MediaDto dto) {
        Media media = mediaMapper.toMedia(dto);

        var savedMedia = mediaRepository.save(media);

        return mediaMapper.toMediaResponseDto(savedMedia);
    }


}
