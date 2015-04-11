def fileToList(path):
    words = []

    file = open(path, 'r')
    for line in file:
        words.append(line)

    return words

def isAnagram(firstWord, secondWord):
    firstWord_chars = list(firstWord)
    firstWord_chars.sort()
    secondWord_chars = list(secondWord)
    secondWord_chars.sort()

    #return (list(firstWord).sort() == list(secondWord).sort())
    return (sorted(firstWord) == sorted(secondWord))

def accumulateAnagrams():
    words = fileToList("unixdict.txt")  
    anagrams = []

    for line in words:
         for otherLine in words:
            if (line != otherLine):
                if (anagrams.count(line) == 0):
                    if (isAnagram(line, otherLine)):
                        anagrams.append(line)

    anagrams.sort()
    return anagrams


for anagram in accumulateAnagrams():
    print(anagram, end='')


