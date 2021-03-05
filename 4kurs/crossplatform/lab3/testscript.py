import lib

print(lib.squareInt(2147483647)) 
print(lib.squareFloat(2))
print(lib.squareDouble(2))
print(lib.squareFloat(float(2)))
print(lib.squareDouble(float(2)))
print(lib.newStringToUppercase("asdf"))
print(lib.mutateStringToUppercase("asdf"))
print(lib.squareInt(2147483647 + 1)) # falls with OverflowError 
