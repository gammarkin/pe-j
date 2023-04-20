import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {
        List < Funcionario > funcionarios = new ArrayList < > ();

        try {
            File arquivo = new File("funcionarios.txt");
            Scanner scanner = new Scanner(arquivo);

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] colunas = linha.split(";");

                String nome = colunas[0];
                LocalDate dataNascimento = LocalDate.parse(colunas[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                BigDecimal salario = new BigDecimal(colunas[2]);
                String departamento = colunas[3];

                Funcionario funcionario = new Funcionario(nome, dataNascimento, salario, departamento);
                funcionarios.add(funcionario);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }

        // Imprime a lista de nomes dos funcionários
        System.out.println("Nomes dos funcionários:");
        for (Funcionario f: funcionarios) {
            System.out.println(f.getNome());
        }
        System.out.println();

        // Remove o funcionário João da lista
        for (Funcionario f: funcionarios) {
            if (f.getNome().equals("João")) {
                funcionarios.remove(f);
                break;
            }
        }

        // Aumenta o salário de todos os funcionários em 10%
        for (Funcionario f: funcionarios) {
            BigDecimal novoSalario = f.getSalario().multiply(new BigDecimal("1.10"));
            f.setSalario(novoSalario);
        }

        // Agrupa os funcionários por função em um mapa
        Map < String, List < Funcionario >> funcionariosPorFuncao = new HashMap < > ();
        for (Funcionario f: funcionarios) {
            String funcao = f.getFuncao();
            if (!funcionariosPorFuncao.containsKey(funcao)) {
                funcionariosPorFuncao.put(funcao, new ArrayList < > ());
            }
            funcionariosPorFuncao.get(funcao).add(f);
        }

        // Imprime os funcionários agrupados por função
        System.out.println("Funcionários agrupados por função:");
        for (Map.Entry < String, List < Funcionario >> entry: funcionariosPorFuncao.entrySet()) {
            System.out.println("Função: " + entry.getKey());
            for (Funcionario f: entry.getValue()) {
                System.out.println(f.getNome());
            }
            System.out.println();
        }

        // Imprime os nomes dos funcionários que fazem aniversário no mês 10 e 12
        System.out.println("Funcionários que fazem aniversário em outubro e dezembro:");
        for (Funcionario f: funcionarios) {
            int mes = f.getDataNascimento().getMonthValue();
            if (mes == 10 || mes == 12) {
                System.out.println(f.getNome());
            }
        }

        System.out.println();

        // Encontra o funcionário com a maior idade
        Funcionario maisVelho = funcionarios.get(0);
        LocalDate hoje = LocalDate.now();
        int iddMaisAlta = 0;

        for (Funcionario f: funcionarios) {
            LocalDate dataNascimento = f.getDataNascimento();
            int idade = hoje.getYear() - dataNascimento.getYear();
            if (dataNascimento.plusYears(idade).isAfter(hoje)) {
                idade--;
            }
            LocalDate dataNascimentoMaisVelho = maisVelho.getDataNascimento();
            int idadeMaisVelho = hoje.getYear() - dataNascimentoMaisVelho.getYear();
            if (dataNascimentoMaisVelho.plusYears(idadeMaisVelho).isAfter(hoje)) {
                idadeMaisVelho--;
            }
            if (idade > idadeMaisVelho) {
                maisVelho = f;
                iddMaisAlta = idade;
            }
        }

        System.out.println("Funcionário mais velho: " + maisVelho.getNome() + ", " + iddMaisAlta + " anos");
        System.out.println();

        // Imprime a lista de nomes dos funcionários em ordem alfabética
        System.out.println("Nomes dos funcionários em ordem alfabética:");
        funcionarios.stream()
            .map(Funcionario::getNome)
            .sorted()
            .forEach(System.out::println);
        System.out.println();

        // Define o valor do salário         mínimo
        BigDecimal salarioMinimo = new BigDecimal("1212.00");

        // Imprime quantos salários mínimos ganha cada funcionário
        System.out.println("Salários mínimos dos funcionários:");
        for (Funcionario f: funcionarios) {
            BigDecimal salarioMinimos = f.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_EVEN);
            System.out.printf("%s: %.2f salários mínimos\n", f.getNome(), salarioMinimos.doubleValue());
        }

        // Calcula o total dos salários dos funcionários
        BigDecimal totalSalarios =
            funcionarios.stream()
            .map(Funcionario::getSalario)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Imprime o total dos salários dos funcionários
        System.out.println("Total dos salários dos funcionários: R$ " +
            totalSalarios.setScale(2, RoundingMode.HALF_EVEN).toString().replace(".", ","));
    }
}