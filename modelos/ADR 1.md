# ADR 01 — Banco Relacional para pediodos e reservas

Autores e data: Sirius Kamphorst 31/10/2026

## Contexto
É esperado que enquanto um pagamento sobre um pedido ainda não foi concluido um pedido se mantenha em reserva, reservando os itens relacionados. Assim como quando exibir o pedido é necessário exibir quais foram os itens do pedido, preço historico, nome, imagem e entre outros, Esse tipo de relação é mais recomendado a utilização de um banco relacional.
## Decisão e requisito atendido
pela necessidade de alto relacionamento entre entidades o banco decidido como mais adequado foi SQL, onde há garantia por meio de Foreing keys que os dados sejam consistentes.

## Alternativa rejeitada
Utilização de banco documental seria uma alternativa, caso o objetivo fosse simplificar o projeto a um banco unico, mas nem a estrategia de armazenamento SQL nem a Documental consegue ser mais adequado que o outro em todas as categorias, logo neste caso precisariamos usar relações não tão asseguradas no banco documental quanto seriam no relacional.
## Compromissos
Manter mais de um tipo de armazenamento significaria deixar mais custoso por necessitar subir dois SGBD.

Uma limitação seria caso entre os requisitos estivesse mostrar os atributos de cada pedido nos produtos, atributos cujo estariam em formato de documento, caso armazenassem um json no database SQL deixaria a consulta mais pesada caso o numero de atributos escalasse, além de deixar o mecanismo de otimização via indices muito mais complexo, caso estivesse em um outro sgbd não relacional documental, ai só a API conseguiria mostrar precisando fazer consultas otimizadas em duas fontes dependendo de ambas para exibição completa da informação.

## Condição de revisão
pode ser revisto caso o objetivo não seja adequar cada funcionalidade da aplicação ao sgbd que mais se adequa a ela, mas sim observar qual contempla melhor a maior parte da aplicação e faz de maneira adequada o restante.



150–220 palavras por ADR; fontes e evidências quando pertinentes.
