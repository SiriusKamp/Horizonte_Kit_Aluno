# ADR 01 — Banco Documental para catalogo e buscas

Autores e data: Sirius Kamphorst 31/10/2026

## Contexto
Um usuario pesquisa um produto por um anuncio que viu em alguma plataforma, mas não sabe o nome do produto, ele pesquisa algo da cabeça dele, é dever da aplicação retornar anuncios cujo tem algo relacionado a pesquisa do usuario, uma informação muito variavel.
Ao querer analisar melhor os produtos pelas imagens, valores e titulos o usuario abre um a um de interessante e analisa as caracteristicas, porém um revendedor pior avaliado colocou poucas informações enquanto um vendedor oficial colocou todos os atributos oficiais do produto. Indiretamente o dinamismo de atributos contribuiu para Busca, Informações, e Integridade de um anuncio.
## Decisão e requisito atendido
Adotar um banco documental para armazenar produtos e seus atributos variáveis em documentos flexíveis. Essa estrutura facilita representar características específicas por categoria e recuperar informações sem exigir que todos os produtos tenham os mesmos campos. Índices de texto e filtros por atributos podem apoiar a busca, recomendações por similaridade semântica exigem também uma solução de busca vetorial apropriada.

## Alternativa rejeitada
Um banco de grafos favoreceria consultas de relacionamento e recomendações baseadas em conexões entre produtos, clientes e compras. Porém, para o armazenamento e a consulta predominante dos atributos e detalhes dos anúncios, adicionaria complexidade sem benefício proporcional.

## Compromissos
Uma limitação é que o sistema de buscas não seria tão eficiente correlacionando produtos com semelhanças quanto o de grafos, Com o de grafos seria possivel fazer buscas recomendadas com base em produtos que sairam no mesmo carrinho, tem semelhanças em atributos e até por pesquisa+compra, porém com o documental cada uma dessas visões necessitaria de uma estrtura dedicada a resolver o requisito, sendo menos indicado para a tarefa e mais custoso de manter e arquiteturar

## Condição de revisão
Seria necessário rever em casos como necessidade de relacionar com muitos tipos de entidades, e poucos atributos dinamicos, ou caso a recomendação fosse o grande diferencial da aplicação, tornando o relacional ou o orientado a grafos melhores opções



150–220 palavras por ADR; fontes e evidências quando pertinentes.
