set title "ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.20512820512820515*x**0)+(7.2490842490842455*x**1)+(-54.99440374440372*x**2)+(135.71937321937315*x**3)+(-139.18142043142038*x**4)+(51.002238502238484*x**5)
set xrange [0:1]
set yrange [0:1]
plot f(x)
