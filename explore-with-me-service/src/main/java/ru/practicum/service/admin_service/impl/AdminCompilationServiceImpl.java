package ru.practicum.service.admin_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequest;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.admin_service.AdminCompilationService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));

        return CompilationMapper.toCompilationDto(
                compilationRepository.save(compilation)
        );
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest request) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);

        if (compilation.isEmpty()) {
            throw new NoSuchElementException("Подборки с идентификатором " + compId + " не существует");
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            compilation.get().setTitle(request.getTitle());
        }

        if (request.getEvents() != null) {
            compilation.get().setEvents(eventRepository.findAllByIdIn(request.getEvents()));
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation.get()));
    }
}
