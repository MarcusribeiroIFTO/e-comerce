package pweb2.controll;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import pweb2.model.entity.Carrinho;

@SessionScope
@Component
public class CarrinhoSession {
    private Carrinho carrinho = new Carrinho();

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }
}
