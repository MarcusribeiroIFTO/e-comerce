package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import pweb2.model.entity.Departamento;

import java.util.List;

@Repository
public class DepartamentoRepository {

    @PersistenceContext
    private EntityManager em;

    public void salvar(Departamento departamento) {
        em.persist(departamento);
    }

    public Departamento findById(Long id) {
        return em.find(Departamento.class, id);
    }

    public List<Departamento> findAll() {
        return em.createQuery(
                "select d from Departamento d",
                Departamento.class
        ).getResultList();
    }

    public void atualizar(Departamento departamento) {
        em.merge(departamento);
    }

    public void remover(Long id) {
        Departamento departamento = findById(id);
        if (departamento != null) {
            em.remove(departamento);
        }
    }
    public List<Departamento> findByNomeContainingIgnoreCase(String nome) {
        return em.createQuery(
                        "SELECT d FROM Departamento d " +
                                "WHERE LOWER(d.nome) LIKE LOWER(CONCAT('%', :nome, '%'))",
                        Departamento.class
                )
                .setParameter("nome", nome)
                .getResultList();
    }

}

