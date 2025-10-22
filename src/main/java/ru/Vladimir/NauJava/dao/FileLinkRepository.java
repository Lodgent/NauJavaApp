package ru.Vladimir.NauJava.dao;

import ru.Vladimir.NauJava.Models.FileLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileLinkRepository extends CrudRepository<FileLink, Long> {
}