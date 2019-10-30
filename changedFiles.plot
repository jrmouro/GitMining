set title "low-pro-for-jquery-ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(-20742.19149084833*x**0)+(113634.45051022751*x**1)+(-221180.87412428923*x**2)+(184445.34042568112*x**3)+(-56156.22532077107*x**4)
set xrange [0:1]
plot f(x)
