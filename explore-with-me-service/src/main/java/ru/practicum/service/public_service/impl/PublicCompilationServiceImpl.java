package ru.practicum.service.public_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.service.public_service.PublicCompilationService;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        if ((from != null && from < 0) || (size != null && size <= 0)) {
            throw new IllegalArgumentException("Неверные параметры поиска");
        }

        if (from == null) {
            from = 0;
        }

        if (size == null) {
            size = Integer.MAX_VALUE;
        }

        if (pinned == null) {
            return compilationRepository
                    .findAll(Pageable.ofSize(from + size))
                    .stream()
                    .skip(from)
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository
                    .findAllByPinned(pinned, Pageable.ofSize(from + size))
                    .stream()
                    .skip(from)
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);

        if (compilation.isEmpty()) {
            throw new NoSuchElementException("Подборки с идентификатором " + compId + " не существует");
        }

        return CompilationMapper.toCompilationDto(compilation.get());
    }
}
