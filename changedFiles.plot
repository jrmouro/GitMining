set title "GitMining - ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(36.20175147397413*x**0)+(-282.9727367194123*x**1)+(709.2041079576344*x**2)+(-721.0822672952797*x**3)+(259.4579681124952*x**4)
set xrange [0:1]
plot f(x)
