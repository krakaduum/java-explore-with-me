package ru.practicum.service.public_service;

import ru.practicum.dto.CompilationDto;

import java.util.Collection;

public interface PublicCompilationService {
    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(long compId);
}
