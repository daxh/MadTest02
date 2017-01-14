# MadTest02

This is a try to implement such common task as displaying a collection of items in a RecyclerView
using Rx with additional goodies such as few itemView types, infinite scrolling, unified and easy
management.

1) I am starting from a **simple implementation** of RecyclerView with basic support for clicks because
in this simple case there is no place to use Rx, as I believe. It will be a good starting point for
further explorations and comparisons. Also, obviously some classes created here will be reused on
the next stages.

2) Then implementation of **RecyclerView with headers** added. As the case above, this could be used
for further explorations and comparisons, some classes created here will be reused on latest stages,
when we will build our uber-adapter.

3) **FilteredRecyclerViewActivity**, and **FilteredRecyclerViewAdapter** respectively, is an example of
how filtering functionality could be added to RecyclerView in a classic way.

4) **RxFilteredRecyclerViewActivity**, and **RxFilteredRecyclerViewAdapter** respectively, is an example of
how filtering functionality could be added to RecyclerView in Rx-style.

5) **RxSortedRecyclerViewActivity**, and **RxSortedRecyclerViewAdapter** respectively, is an example of
how sorting functionality could be added to RecyclerView in Rx-style.
