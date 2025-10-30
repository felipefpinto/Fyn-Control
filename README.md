# 💰 Fyn Control — App de Controle Financeiro

O **Fyn Control** é um aplicativo Android simples e eficiente para ajudar no **gerenciamento financeiro pessoal**, permitindo registrar **entradas** e **saídas**, visualizar o **saldo total** e acompanhar movimentações facilmente.

O app foi desenvolvido utilizando **Kotlin** e **SQLite**, com interface moderna baseada em **Material Design**.

---

## 🎯 Funcionalidades

| Função | Descrição |
|-------|-----------|
| 🔐 Login Seguro | Acesso através de tela de login estilizada |
| 💵 Registro Financeiro | Permite adicionar **entradas** e **saídas** |
| 📊 Cálculo Automático | Exibe total de Entradas, Saídas e Saldo |
| 🎨 Barra de Saldo Dinâmica | A cor muda automaticamente conforme o saldo (verde, amarelo ou vermelho) |
| 🗂 Lista de Transações | Visualize todas as movimentações registradas |
| 🗑 Exclusão de Registros | Apague qualquer transação facilmente |
| 🧭 Interface Moderna | UI semelhante a aplicativos financeiros reais |

---

## 🖥️ Telas do Aplicativo

| Tela | Descrição |
|------|-----------|
| Tela de Login | Entrada do usuário com design moderno |
| Tela Financeira | Página principal com totais e lista de transações |

---

## 🧱 Estrutura do Banco de Dados (SQLite)

Tabela: **Transacoes**

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | INTEGER (PK) | Identificador único |
| `descricao` | TEXT | Título da transação |
| `valor` | REAL | Valor financeiro |
| `tipo` | TEXT ("Entrada" ou "Saída") | Define se soma ou subtrai |

---

## 🛠️ Tecnologias Utilizadas

- Kotlin
- Android Studio
- SQLite
- Material Design Components

---
## 🎨 Ícone do App

O projeto inclui um **ícone adaptativo com máscara circular**, compatível com:

- 📱 Android 8+ (Adaptive Icons)
- 🟢 Tema claro/escuro
- 🛍 Play Store Upload (512×512 incluído)

---

## 🔧 Como Executar

1. Baixe o projeto e abra no **Android Studio**
2. Aguarde sincronizar as dependências
3. Execute em um **Emulador** ou **Dispositivo físico**
4. Faça login com:
   ```
   Usuário: admin
   Senha: 1234
   ```

---

## 📝 Licença

Este projeto é **livre para uso educacional e pessoal**.  
Sinta-se à vontade para modificar, evoluir e publicar sua própria versão ⭐
