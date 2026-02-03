package pweb2.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.Carrinho;
import pweb2.model.entity.Produto;
import pweb2.model.repository.ProdutoRepository;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/produtos")
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoSession carrinhoSession;

    @PostMapping("/adicionar-ao-carrinho")
    public Map<String, Object> adicionarAoCarrinho(
            @RequestParam Long produto_id,
            @RequestParam int quantidade){

        Map<String, Object> response = new HashMap<>();

        Produto produto =produtoRepository.findById(produto_id);

        if (produto == null){
            response.put("sucess", false);
            response.put("message", "Produto não encontrado");
            return  response;
        }
        Carrinho carrinho = carrinhoSession.getCarrinho();
        carrinho.adicionarProduto(produto, quantidade);

        response.put("sucess",false);
        response.put("message", "Produto adicionado ao carrinho");
        return response;
    }

    @PostMapping("/atualizarCarrinho")
    public Map<String, Object> atualizarCarrinho(
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {

        Map<String, Object> response = new HashMap<>();

        Produto produto = produtoRepository.findById(produtoId);

        if (produto == null) {
            response.put("success", false);
            response.put("message", "Produto não encontrado");
            return response;
        }

        carrinhoSession.getCarrinho().atualizarItem(produto, quantidade);
        response.put("success", true);
        response.put("message", "Quantidade atualizada!");
        return response;

    }


}