Jembi Registration Module
=========================

Features
--------
* Separates *Find Patient* and *Register Patient* functionality

> The module adds a *Register Patient* gutter link that takes the user to the patient registration 
> page.

* Enables single page registration

> Patient registration is now done on a single page, eliminating the short form.

* Enables barcode printing

> A button is added to the patient dashboard header that prints a label with a barcode and basic
> demographic information.

Install Guide
-------------
**Tested on Windows only, using a [Zebra
GK420t](http://www.zebra.com/us/en/products-services/printers/printer-type/desktop/g-series-gk.html)
barcode printer**

* Printer Installation

> Install the printer as a **Generic/Text** printer from the Add New Devices menu (printing to 
> USB001) and give the printer a name. See the *Generic Printer Installation Walk Through on Windows7*
> section [here](http://www.shiprush.com/Product_Documentation/ShipRush_v9-0_USPS/Content/Thermal_Printer_Installation.htm)
> for additional instructions. There is no need to install the Zebra printer drivers. The printer
> user manual can be found [here](http://www.zebra.com/content/dam/zebra/manuals/en-us/printer/gk420t-ug-en.pdf).

* Module Installation

> First install and configure the [Address Hierarchy Module](https://wiki.openmrs.org/display/docs/Address+Hierarchy+Module)
> (version 2.2.8 or later), then upload the module using the web interface.

* Module Configuration

> Under the module's [global properties](https://wiki.openmrs.org/display/docs/Global+Properties+Descriptions),
> add the printer name and the Patient Identifier Type id that you want to use. You can also configure
> the number of copies to print.

User Guide
----------

* Patient Registration

> Click the *Register Patient* gutter link. Complete the form and click save.

* Patient Search

> Click the *Find/Create Patient* link in the gutter. Type in a patient name, identifier or scan
> a barcode to search for a patient. Click on the patient to navigate to the patient's dashboard.

* Barcode Printing

> Click the *Print Barcode* button in the header on the patient dashboard.


