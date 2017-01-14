# MadTest02

This is a try to implement such common task as displaying a collection of items in a RecyclerView
using Rx with additional goodies such as few itemView types, infinite scrolling, unified and easy
management.

1. I am starting from a **simple implementation of RecyclerView** with basic support for clicks because
in this simple case there is no place to use Rx, as I believe. It will be a good starting point for
further explorations and comparisons. Also, obviously some classes created here will be reused on
the next stages.

2. Then implementation of **RecyclerView with headers** added. As the case above, this could be used
for further explorations and comparisons, some classes created here will be reused on latest stages,
when we will build our uber-adapter.

3. After that implementation of **RecyclerView with mutable data** added. This too could be used for
further explorations and comparisons, some classes created here will be reused on latest stages,
when we will build our uber-adapter.

4. **Filtering**

    1. **RecyclerView with filtering** in general based on **simple implementation of RecyclerView** with
    one idea taken from **RecyclerView with headers** - we use two fields to store inner dataset in adapter:
    one of them to keep original data, the next one to keep and show filtered data. This is an example of
    how filtering functionality could be added to RecyclerView in a classic way.

    2. **RecyclerView with filtering in Rx-stryle** is based on classic **RecyclerView with filtering** but
    ItemFilter replaced with Rx-chain and some other nice tweeks done.

5. **Sorting**

    1. **RecyclerView with sorting** in general based on **simple implementation of RecyclerView** with
    one idea taken from **RecyclerView with headers** - we use two fields to store inner dataset in adapter:
    one of them to keep original data, the next one to keep and show sorted data. This is an example of
    how sorting functionality could be added to RecyclerView in a classic way.

    2. **RecyclerView with sorting in Rx-stryle** is based on classic **RecyclerView with sorting** but
    ItemFilter replaced with Rx-chain and some other nice tweeks done.
