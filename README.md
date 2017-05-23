# Zendesk Plugin for PDI

[![Build Status](https://travis-ci.org/matthewtckr/pdi-zendesk-plugin.svg)](https://travis-ci.org/matthewtckr/pdi-zendesk-plugin)

## Overview

The Zendesk Plugin for PDI is designed to retrieve audit information about Zendesk tickets, users, organizations, and HelpCenter content, allowing businesses to incorporate this data into existing data warehouses.

Aside from retrieving data from Zendesk, there are some additional steps that allow updating data in Zendesk:

* Suspend or remove suspension of users
* Delete suspended tickets

## Installation

### PDI Spoon Client:
1. Launch Spoon
2. Open the Marketplace from the 'Tools' or 'Help' menu
3. In the search box, type "PDI Zendesk Plugin"
4. Click the Install button
5. Close and restart Spoon

### Pentaho Server:
1. Login to the Pentaho Server website as an administrator
2. In the left drop-down navigation, select "Marketplace"
3. In the search box, type "PDI Zendesk Plugin"
4. Click the Install button
5. Restart the Pentaho Server

## Examples

As most data types within Zendesk do not change often (e.g. Groups, Help Center Categories, Custom Fields, etc.), in most cases developers may choose to simply fetch all records within the data types, and Insert/Update the records as appropriate.

Some data types in Zendesk are constantly receiving new records or updates, such as Organizations, Users, Tickets, and HelpCenter articles.  To efficiently fetch only the records that have been created or changed recently, a developer can pass in the date of the last successful request, and the Incremental Input step will return all of the ID numbers that have been created or updated.  Another step can then be used to fetch the details of those tickets, users, organizations, or HelpCenter articles.

## Support

The Zendesk Plugin for PDI is provided as-is, with no warranties, expressed or implied.  This Plugin is not covered by any Support Agreements.
