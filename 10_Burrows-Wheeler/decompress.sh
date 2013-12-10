#!/bin/bash

java  Huffman + < $1 | java MoveToFront + | java BurrowsWheeler + > $2

