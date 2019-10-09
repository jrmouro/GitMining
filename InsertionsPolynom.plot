set title "Insertions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.15196078431372548*x**0)+(-3.5204761197408256*x**1)+(25.828113652868556*x**2)+(-82.1696039526922*x**3)+(128.65782887228966*x**4)+(-97.94195455960163*x**5)+(28.994131322562698*x**6)
set xrange [0:1]
set yrange [0:1]
plot f(x)
