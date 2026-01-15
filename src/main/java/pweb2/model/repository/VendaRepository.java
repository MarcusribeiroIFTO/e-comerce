package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb2.model.entity.Venda;

import java.util.List;

@Repository
@Transactional
public class VendaRepository {
    @PersistenceContext
    private EntityManager em;

    public void salvar(Venda venda) {
        if (venda.getId() == null){
            em.persist(venda);
        }else {
            em.merge(venda);
        }
    }
    public Integer buscarUltimoNumeroVenda() {
        return em.createQuery(
                "select max(v.numeroVenda) from Venda v", Integer.class
        ).getSingleResult();
    }
    public Venda findById(Long id) {
        return em.find(Venda.class, id);
    }

    public List<Venda> findAll() {
        return em.createQuery("from Venda ", Venda.class).getResultList();
    }
    public void excluir(Long id) {
        Venda venda = em.find(Venda.class, id);
        if (venda != null) {
            em.remove(venda);
        }
    }
    public void editar(Venda venda) {
        em.merge(venda);
    }
}
