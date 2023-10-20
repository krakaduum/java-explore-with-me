package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.model.Compilation;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation
                        .getEvents()
                        .stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toSet()),
                compilation.getPinned(),
                compilation.getTitle());
    }

    public static Compilation toCompilation(CompilationDto compilationDto) {
        return new Compilation(
                compilationDto.getId(),
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                compilationDto
                        .getEvents()
                        .stream()
                        .map(EventMapper::toEvent)
                        .collect(Collectors.toSet()));
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return new Compilation(
                compilationDto.getTitle(),
                compilationDto.isPinned());
    }

    public static Compilation toCompilation(UpdateCompilationRequest request) {
        return new Compilation(
                request.getTitle(),
                request.isPinned());
    }
}
