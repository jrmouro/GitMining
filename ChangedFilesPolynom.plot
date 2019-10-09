set title "ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.21428571428571427*x**0)+(-7.544015225137672*x**1)+(68.43949087571536*x**2)+(-259.6827421444768*x**3)+(472.9426492819348*x**4)+(-409.1755749919014*x**5)+(134.8059064895799*x**6)
set xrange [0:1]
set yrange [0:1]
plot f(x)
