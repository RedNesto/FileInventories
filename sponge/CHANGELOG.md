# Changelog

## 0.3.2
          
- fixed players being kicked when joining if they have file items in their inventory

## 0.3.1

- added color codes support for `lore`

## 0.3.0

- `openInventory` now returns an `Optional<Inventory>`
- added `offerItem(String, Player)`
- added `offerItem(String, Player, Inventory)`
- added middle click support
- renamed everything from `right` to `secondary` and `left` to `primary:`
  - `on_inv_right_click` -> `on_inv_secondary_click`
  - `on_inv_left_click` -> `on_inv_primary_click`
  - added `on_inv_middle_click`
  - `on_right_click` -> `on_secondary_click`
  - `on_left_click` -> `on_primary_click`
  - added `on_middle_click`
  - `on_interact_right_click` -> `on_interact_secondary_click`
  - `on_interact_left_click` -> `on_interact_primary_click`
