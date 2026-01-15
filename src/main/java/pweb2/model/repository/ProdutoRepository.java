package pweb2.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import pweb2.model.entity.Produto;

import java.util.List;

@Repository
@Transactional
public class ProdutoRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Produto produto) {
        if (produto.getId() == null) {
            em.persist(produto);
        } else {
            em.merge(produto);
        }
    }

    public Produto findById(Long id) {
        return em.find(Produto.class, id);
    }

    public List<Produto> findAll() {
        return em.createQuery("from Produto", Produto.class).getResultList();
    }

    public void deletar(Long id) {
        Produto produto = findById(id);
        if (produto != null) {
            em.remove(produto);
        }
    }
}
