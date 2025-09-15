package com.itravel.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itravel.backend.models.Page;
import com.itravel.backend.repository.PageRepository;

@Service
public class PageService {

    @Autowired
    PageRepository pageRepository;

    public List<Page> findAll() {
        return pageRepository.findAll();
    }

    public Optional<Page> findById(Long id) {
        return pageRepository.findById(id);
    }

    public Page create(Page page) {
        return pageRepository.save(page);
    }

    public void deleteById(Long id) {
        pageRepository.deleteById(id);
    }
}
