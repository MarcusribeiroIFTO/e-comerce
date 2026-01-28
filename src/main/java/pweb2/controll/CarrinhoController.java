package pweb2.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pweb2.model.entity.Carrinho;
import pweb2.model.entity.Produto;
import pweb2.model.repository.ProdutoRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/produtos")
public class CarrinhoController {
    @Autowired
    private CarrinhoSession carrinhoSession;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/adicionar-no-carrinho")
    public Map<String, Object> adicionarNoCarrinho(@RequestParam Long produtoId,
                                                   @RequestParam int quantidade){
        Map<String, Object> response = new HashMap<>();

        Produto produto = produtoRepository.findById(produtoId);

        if (produto == null){
            response.put("sucess", false);
            response.put("message", "Produto não encontrado");
            return response;
        }
        Carrinho carrinho = carrinhoSession.getCarrinho();
        response.put("sucess", true);
        response.put("message", "Quantidade atualizada");
        return response;
    }
}
