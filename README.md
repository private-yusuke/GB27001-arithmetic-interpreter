# ソフトウェアサイエンス特別講義A（GB27001） プログラミング⾔語作成概論 課題

## インタプリタの実装について

Kotlin を用いて、トークナイザ・パーサ・評価器およびそれらを組み合わせたインタープリタを実装した。

## 自動テストについて

GitHub Actions を用いることで、リポジトリの push ごとに自動的にテストが実行されるようにした。

## Context Receivers を用いた実装

ref: https://github.com/Kotlin/KEEP/blob/c538992d33a8d4f0d7b027ee3cb0610c92821046/proposals/context-receivers.md

この文章がある差分では Context Receivers 機能を用いて `Parser` の実装を記述しています。
