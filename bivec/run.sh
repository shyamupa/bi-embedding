make -f makefile clean
make -f makefile
if [ ! -d "output" ]; then
  mkdir output
fi

command="./bivec -src-train $1 -src-lang $2 -tgt-train $3 -tgt-lang $4 -align $5 -output output/vectors -cbow 0 -size 200 -window 5 -negative 0 -hs 1 -sample 1e-3 -tgt-sample 1e-3 -threads 5 -binary 0 -eval 1 -iter 3 -bi-weight 1.0 -align-opt 1"
echo "time $command"
time $command

