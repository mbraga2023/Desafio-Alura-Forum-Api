package com.alura.Desafio_Forum.repository;

import com.alura.Desafio_Forum.domain.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    boolean existsByTitulo(String titulo);

    boolean existsByMensagem(String mensagem);

    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :cursoNome AND FUNCTION('YEAR', t.data_criacao) = :ano")
    Page<Topico> findByCursoNomeAndAno(String cursoNome, int ano, Pageable pageable);

    @Query("SELECT t FROM Topico t ORDER BY t.data_criacao ASC")
    Page<Topico> findAllByOrderByDataCriacaoAsc(Pageable pageable);

    boolean existsByTituloAndMensagemAndCursoId(String titulo, String mensagem, Long id);

    Page<Topico> findByStatusTrue(Pageable pageable);

    Optional<Topico> findByIdAndStatusTrue(Long id);
}
