package br.edu.prova.produtos.controller;

import br.edu.prova.produtos.model.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProdutoController {

    private final List<Produto> produtos = new ArrayList<>();
    private Long proximoId = 4L;

    public ProdutoController() {
        produtos.add(new Produto(1L, "Notebook Essencial", "Notebook para estudos e trabalho.", 3200.00, "Informática", "https://placehold.co/600x400?text=Notebook"));
        produtos.add(new Produto(2L, "Mouse Sem Fio", "Mouse ergonômico com conexão USB.", 89.90, "Acessórios", "https://placehold.co/600x400?text=Mouse"));
        produtos.add(new Produto(3L, "Teclado Mecânico", "Teclado mecânico para produtividade e jogos.", 249.90, "Acessórios", "https://placehold.co/600x400?text=Teclado"));
    }


    @GetMapping
    public List<Produto> listar() {
        return produtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        Optional<Produto> encontrado = produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (encontrado.isPresent()) {
            return ResponseEntity.ok(encontrado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        produto.setId(proximoId++);
        produtos.add(produto);
        return ResponseEntity.status(201).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        for (Produto p : produtos) {
            if (p.getId().equals(id)) {
                p.setNome(produtoAtualizado.getNome());
                p.setDescricao(produtoAtualizado.getDescricao());
                p.setPreco(produtoAtualizado.getPreco());
                p.setCategoria(produtoAtualizado.getCategoria());
                p.setImagemUrl(produtoAtualizado.getImagemUrl());
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        boolean removido = produtos.removeIf(p -> p.getId().equals(id));

        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
