set title "GitMining - Insertions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(42.07084635268646*x**0)+(-332.3454867961655*x**1)+(835.5260117181931*x**2)+(-843.9005736219024*x**3)+(299.626375305924*x**4)
set xrange [0:1]
plot f(x)
