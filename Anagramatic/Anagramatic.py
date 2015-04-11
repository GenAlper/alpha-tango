def fileToList(path):
    words = []

    file = open(path, 'r')
    for line in file:
        words.append(line[:-1])

    return words

def isAnagram(firstWord, secondWord):
    return (sorted(firstWord) == sorted(secondWord))

def accumulateAnagrams():
    words = fileToList("unixdict.txt")  
    anagrams = []

    for line in words:
         for otherLine in words:
            if (line != otherLine):
                if (anagrams.count((otherLine, line)) == 0):
                    if (isAnagram(line, otherLine)):
                        anagrams.append((line, otherLine))

                     
    return sorted(anagrams, key=lambda t: len(t[1]))



anagrams = accumulateAnagrams()
for anagram in anagrams:
    print("%s <-> %s" % anagram)
print("Number of anagrams found: ", len(anagrams))

