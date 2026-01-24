package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pweb2.model.entity.Pessoa;
import pweb2.model.entity.PessoaFisica;
import pweb2.model.entity.PessoaJuridica;

import java.util.ArrayList;
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
    public void delete(Long id) {
        Pessoa p = em.find(Pessoa.class, id);
        if (p != null) {
            em.remove(p);
        }
    }
    public void update(Pessoa p) {
        em.merge(p);
    }
    public void deleteAll() {
        em.createQuery("DELETE FROM Pessoa p").executeUpdate();
    }

    public List<Pessoa> findByNomeOuRazaoSocial(String nome) {
        List<Pessoa> resultado = new ArrayList<>();

        Query queryFisica = em.createQuery(
                "SELECT p from PessoaFisica p where  LOWER(p.nome) LIKE lower(concat('%', :nome, '%'))"
        );
        queryFisica.setParameter("nome", nome);
        resultado.addAll(queryFisica.getResultList());

        Query queryJuridica = em.createQuery(
                "SELECT p from PessoaJuridica  p where  lower(p.razaoSocial) like lower(concat('%', :nome, '%'))"
        );
        queryJuridica.setParameter("nome", nome);
        resultado.addAll(queryJuridica.getResultList());
        return resultado;
    }
    public Pessoa findByEmail(String email) {
        try{
            return em.createQuery("select p from Pessoa p where lower(p.email) = lower(:email)", Pessoa.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    public Pessoa findByResetSenhaToken(String token) {
        try{
            return em.createQuery("select p from Pessoa p where p.resetSenha = :token", Pessoa.class)
                    .setParameter("token", token)
                    .getSingleResult();
        }catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
}
