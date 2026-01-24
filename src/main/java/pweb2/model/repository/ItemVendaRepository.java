package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import pweb2.model.entity.ItemVenda;

import java.util.List;

@Repository
public class ItemVendaRepository {

    @PersistenceContext
    private EntityManager em;

    public void salvar(ItemVenda item){
        em.persist(item);
    }
    public List<ItemVenda> buscarPorVenda(Long vendaId){
        return em.createQuery("select item from ItemVenda item " +
                        "where item.venda.id = :vendaId", ItemVenda.class)
                .setParameter("vendaId", vendaId)
                .getResultList();
    }
    public void removerPorVenda(Long vendaId){
        em.createQuery("delete from ItemVenda  item where  item.venda.id = :vendaId")
                .setParameter("vendaId", vendaId)
                .executeUpdate();
    }
}
