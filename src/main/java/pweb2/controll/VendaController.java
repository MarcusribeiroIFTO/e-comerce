package pweb2.controll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pweb2.model.entity.ItemVenda;
import pweb2.model.entity.Pessoa;
import pweb2.model.entity.Produto;
import pweb2.model.entity.Venda;
import pweb2.model.repository.PessoaRepository;
import pweb2.model.repository.ProdutoRepository;
import pweb2.model.repository.VendaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vendas")
@SessionAttributes("venda")
public class VendaController {


    private VendaRepository vendaRepository;
    private ProdutoRepository produtoRepository;
    private PessoaRepository pessoaRepository;
    private CarrinhoSession carrinhoSession;

    public VendaController(VendaRepository vendaRepository,
                           ProdutoRepository produtoRepository,
                           PessoaRepository pessoaRepository,
                           CarrinhoSession carrinhoSession) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.pessoaRepository = pessoaRepository;
        this.carrinhoSession = carrinhoSession;
    }

    @ModelAttribute("venda") //inicializar a sessão da venda
    public Venda vendaSetup() {
        Venda venda = new Venda();
        venda.setItens(new ArrayList<>());
        return Venda;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaRepository.findAll());
        return "list";
    }

    @GetMapping("/novo")
    public String novo(Model model, @ModelAttribute("venda")Venda venda) {
        model.addAttribute("clientes", pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        return "venda/form";
    }
    @PostMapping("/adicionar-item")
    public String adicionarItem(@RequestParam Long produtoId,
                                @RequestParam Integer quantidade,
                                @ModelAttribute("venda") Venda venda,
                                Model model) {

        Produto produto = produtoRepository.findById(produtoId);

        if (produto != null) {
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setVenda(venda);
            venda.getItens().add(item);
        }
        model.addAttribute("clientes",  pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());

        return "venda/form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam Long clienteId,
                         @ModelAttribute("venda") Venda venda,
                         SessionStatus status) {

        Pessoa cliente = pessoaRepository.findById(clienteId);
        venda.setCliente(cliente);

        for (ItemVenda item : venda.getItens()) {
            item.setVenda(venda);
        }

        vendaRepository.salvar(venda);
        status.setComplete();// atributo para limpar a sessão
        venda.calcularTotal();
        vendaRepository.salvar(venda);
        return "redirect:/vendas";
    }

    @GetMapping("/deletarItem")
    public String deletarItem(@RequestParam int index,
                              @ModelAttribute("venda") Venda venda,
                              Model model) {
        if (index >=0 && index < venda.getItens().size()){
            venda.getItens().remove(index);
        }
        model.addAttribute("clientes",  pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll());
        return "venda/form";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Venda venda = vendaRepository.findById(id);
        if (venda == null) {
            return "redirect:/vendas";
        }
        model.addAttribute("venda", venda);
        return "detalhes";
    }

    @GetMapping("/filtrar")
    public String filtrar(@RequestParam(required = false)String nomeCliente,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                          Model model) {
        List<Venda> vendas;

        if (nomeCliente != null && !nomeCliente.trim().isEmpty() && inicio != null & fim != null) {
            List<Pessoa> clientes = pessoaRepository.findById()
        }
    }

}
