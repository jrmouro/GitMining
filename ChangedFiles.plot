set title "ChangedFiles"
set xlabel "tempo"
set ylabel "volume"
set grid
plot "/home/ronaldo/Documentos/GitMining/ChangedFiles.txt" using 1:2 title 'Changed' with lines
