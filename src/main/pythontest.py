import sys
import pandas as pd
file = sys.argv[1]

data = pd.read_csv(file,sep="\t")

print(data)