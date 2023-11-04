package ru.amiranify.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.amiranify.springcourse.models.Book;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleStartingWith(String title);
}