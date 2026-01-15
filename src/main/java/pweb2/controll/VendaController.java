package pweb2.controll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.ItemVenda;
import pweb2.model.entity.Pessoa;
import pweb2.model.entity.Produto;
import pweb2.model.entity.Venda;
import pweb2.model.repository.PessoaRepository;
import pweb2.model.repository.ProdutoRepository;
import pweb2.model.repository.VendaRepository;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaRepository.findAll());
        return "venda/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("venda", new Venda());
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("pessoas", pessoaRepository.findAll());
        return "venda/form";
    }
    @PostMapping("/adicionar-item")
    public String adicionarItem(
            @RequestParam Long produtoId,
            @RequestParam Integer quantidade,
            @ModelAttribute("venda") Venda venda,
            Model model) {

        Produto produto = produtoRepository.findById(produtoId);

        ItemVenda item = new ItemVenda();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setValorUnitario(produto.getValor());

        venda.addItem(item);

        model.addAttribute("venda", venda);
        model.addAttribute("produtos", produtoRepository.findAll());

        return "venda/form";
    }

    @PostMapping("/salvar")
    public String salvarVenda(
            @ModelAttribute Venda venda,
            @RequestParam Long clienteId,
            @RequestParam(required = false) Long produtoId,
            @RequestParam(required = false) Integer quantidade,
            @RequestParam(required = false) String acao,
            Model model) {

        Pessoa cliente = pessoaRepository.findById(clienteId);
        venda.setCliente(cliente);
        
        if (venda.getId() == null) {
            Integer ultimoNumero = vendaRepository.buscarUltimoNumeroVenda();
            Integer proximoNumero = (ultimoNumero == null) ? 1 : ultimoNumero + 1;
            venda.setNumeroVenda(proximoNumero);

            vendaRepository.salvar(venda);
        }
        if (produtoId != null && quantidade != null && quantidade > 0) {
            Produto produto = produtoRepository.findById(produtoId);

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setValorUnitario(produto.getValor());
            item.setVenda(venda);

            venda.getItens().add(item);
        }

        venda.calcularTotal();
        vendaRepository.salvar(venda);

        if ("continuar".equals(acao)) {
            model.addAttribute("venda", venda);
            model.addAttribute("pessoas", pessoaRepository.findAll());
            model.addAttribute("produtos", produtoRepository.findAll());
            return "venda/form";
        }

        return "redirect:/vendas";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesVenda(@PathVariable Long id, Model model) {
        Venda venda = vendaRepository.findById(id);
        if (venda == null) {
            return "redirect:/vendas";
        }
        model.addAttribute("venda", venda);
        return "venda/detalhes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Venda venda = vendaRepository.findById(id);
        model.addAttribute("venda", venda);
        //model.addAttribute("venda", vendaRepository.findById(id));
        model.addAttribute("pessoas", pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        return "venda/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        vendaRepository.excluir(id);
        return "redirect:/vendas";
    }
}
