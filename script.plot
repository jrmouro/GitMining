set title "CommitDiff"
set xlabel "tempo"
set ylabel "volume"
set grid
plot "/home/ronaldo/Dropbox/GitMining/data.txt" using 1:2 title 'Changed' smooth bezier, "/home/ronaldo/Dropbox/GitMining/data.txt" using 1:3 title 'Insertions' smooth bezier, "/home/ronaldo/Dropbox/GitMining/data.txt" using 1:4 title 'Deletions' smooth bezier
