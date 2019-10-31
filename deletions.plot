set title "GitMining - Deletions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(71.18184046695875*x**0)+(-564.5288393261504*x**1)+(1428.4340550302313*x**2)+(-1467.954767376331*x**3)+(533.7004156078068*x**4)
set xrange [0:1]
plot f(x)
