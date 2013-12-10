#!/bin/bash

java BurrowsWheeler - < $1 | java MoveToFront - | java Huffman - > $2
