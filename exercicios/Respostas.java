import java.util.*;
public class Respostas {
    
 public static String catalogoSql(){ // TODO_ALUNO T1 E4: placeholders ? categoria, minimo, maximo
    return "SELECT id,nome,preco_atual FROM produto WHERE categoria=? AND preco_atual BETWEEN ? AND ?";
 }
 public static String modulo(long h,List<String> nodes){ 
int indice = (int) Long.remainderUnsigned(h, nodes.size());
    return nodes.get(indice);
    }
    
 public static String sucessor(long h,List<App.Point> points){ 
    for (App.Point point : points) {
        if (Long.compareUnsigned(point.hash(), h) >= 0) {
            return point.node();
        }
    }
    return points.get(0).node();
 }
 public static long movidas(Map<String,String>a,Map<String,String>b){
   Long movidas = (long) 0;
   for (String k : a.keySet()) {
       String nodeA = a.get(k);
       String nodeB = b.get(k);
        if (!Objects.equals(nodeA, nodeB)) {
                    movidas++;
       }
   }
   return movidas;
}
 public static String proxima(String state){ 
 switch (state) {
        case "PENDENTE":
            return "reservar";

        case "RESERVADO":
            return "pagar";

        case "PAGO":
            return "confirmar";

        case "REJEITADO":
            return "liberar";

        case "CONFIRMADO":
        case "CANCELADO":
            return "fim";

        default:
            throw new IllegalArgumentException(
                "Estado inválido: " + state
            );
    }
 }

 public static String aposPagamento(String resultado){ 
switch (resultado) {
        case "aprovado":
            return "PAGO";

        case "rejeitado":
            return "REJEITADO";

        default:
            throw new IllegalArgumentException("Resultado de pagamento inválido: " + resultado);
    }
 }

 public static String liberarSql(){ 
return """
        UPDATE reserva
        SET estado = 'LIBERADA'
        WHERE op = ?
          AND estado = 'ATIVA'
        RETURNING op
        """;
     }
}
