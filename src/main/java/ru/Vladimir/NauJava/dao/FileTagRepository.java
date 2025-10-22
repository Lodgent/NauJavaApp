package ru.Vladimir.NauJava.dao;

import ru.Vladimir.NauJava.Models.FileTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTagRepository extends CrudRepository<FileTag, Long> {
}