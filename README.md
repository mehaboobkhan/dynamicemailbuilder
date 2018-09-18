# dynamicemailbuilder
An emailbuilder which aspects an email template with angular.js tags for placeholder and a json with value of these placeholders, building an email dynamically with these inputs.

Supports 2 of angular.js tags:
  1. {{username}}
  2. <tr *ngFor="let hero of heroes">
          <td>{{hero.name}}</td>
      </tr>
 
 Json for above tags would be:
 {
    "username" : "Peter",
    "heroes" : ["superman", "spiderman", "batman"] 
    }
 
 The code will process the inputs and builds an email.
 
 Nested ngFor is also supported 
