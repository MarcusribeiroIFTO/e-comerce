package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pweb2.model.entity.Pessoa;
import pweb2.model.entity.PessoaFisica;
import pweb2.model.entity.PessoaJuridica;

import java.util.List;

@Repository
@Transactional
public class PessoaRepository {

    @PersistenceContext
    private EntityManager em;

    public Pessoa save(Pessoa p) {
        if (p.getId() == null) {
            em.persist(p);
            return p;
        } else {
            return em.merge(p);
        }
    }

    public Pessoa findById(Long id) {
        return em.find(Pessoa.class, id);
    }

    public List<Pessoa> findAll() {
        return em.createQuery("SELECT p FROM Pessoa p", Pessoa.class).getResultList();
    }

    public List<PessoaFisica> findAllFisicas() {
        return em.createQuery("SELECT p FROM PessoaFisica p", PessoaFisica.class).getResultList();
    }

    public List<PessoaJuridica> findAllJuridicas() {
        return em.createQuery("SELECT p FROM PessoaJuridica p", PessoaJuridica.class).getResultList();
    }

    public void delete(Long id) {
        Pessoa p = em.find(Pessoa.class, id);
        if (p != null) {
            em.remove(p);
        }
    }
}
