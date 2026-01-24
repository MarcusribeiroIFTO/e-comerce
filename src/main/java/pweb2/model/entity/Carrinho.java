package pweb2.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrinho implements Serializable {
    private List<ItemVenda> itens = new ArrayList<ItemVenda>();

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
    public void adicionarItem(ItemVenda item){
        for (ItemVenda itemVenda : itens) {
            if (itemVenda.getProduto().equals(itemVenda.getProduto())){
                itemVenda.setQuantidade(itemVenda.getQuantidade() + item.getQuantidade());
                return;
            }
        }
        itens.add(item);
    }
    public void removerItem(Produto produto){
        itens.removeIf(item -> item.getProduto().equals(produto));
    }
    public void atualizarItem(Produto produto, int quantidade){
        if (quantidade <= 0){
            removerItem(produto);
            return;
        }
        for (ItemVenda itemVenda : itens) {
            if (itemVenda.getProduto().equals(produto)){
                itemVenda.setQuantidade(quantidade);
                return;
            }
        }
    }

    public double getValorTotal(){
        return itens.stream()
                .mapToDouble(item -> item.getProduto().getValor().doubleValue() * item.getQuantidade()).sum();
    }

    public void limpar(){
        itens.clear();
    }
}
