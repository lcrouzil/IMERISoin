#-*- coding: utf-8 -*-
'''
0. secure you have 2 modules in tbhe same directory as this py code:
    - features    list of interfaces
    - database    list of database interfaces
1. install fast API :
    pip install fastapi
2. install a prod server (in this case uvicorn)
    pip install uvicorn[standard]
3. run the server (don't forget to move to the current .py file
    before executing uvicorn order):
    uvicorn myFastAPI:app 
4. open your browser
    127.0.0.1:8000
    127.0.0.1:8000/items/81?q=myQuery
'''
from fastapi import FastAPI
from fastapi.responses import HTMLResponse
import features


tagsMetaData      = [] # will contain the tags/desciption for verbs


#
# define general tags for the site
#
try:
    title = features.CONST_title
except:
    title = 'ImeriSoin API'
    
try:
    description = features.CONST_description
except:
    description = 'Projet mené par Clovis CORDE/Laurent Crouzil/Alexis Devleeschauwer/Omaima MADMOURH/Marc RICHARD'
    
try:
    version = features.CONST_version
except:
    version = '0.0.1'
    
app = FastAPI(title       = title,
              description = description,
              version     = version,
              openapi_tags=tagsMetaData)

#
# retrieve functions in features module that will provide the verbs of the web service site
#
for f in vars(features).values():
    # we keep only functions,
    # not started with _ as a name (internal functions fo features module),
    # from the features module
    if     type(f).__name__  == "function" \
       and f.__name__[0]     != '_'    \
       and f.__module__      == "features":
        # we assume the functions for URI are correctly documented
        # first line is the tag and the other lines the description
        lines = f.__doc__.split("\n")
        tag   = lines.pop(0)
        doc   = "\n".join(lines)
        tagsMetaData.append({"name": tag,"description": doc})
        # we start with the global URI ("?option=..." URI)
        URI     = "/"+f.__name__
        app.get(URI,  tags=[tag])(f)
        # and then we add URI with options
        # (URI like function/parameter1/parameter2/...)
        nbArg = f.__code__.co_argcount
        if  f.__defaults__ != None: nbDef = len(f.__defaults__ )
        for k in range(f.__code__.co_argcount):
            var = f.__code__.co_varnames[k]
            dft = ''
            #if f.__defaults__ != None: dft = f.__defaults__[k]
            if  f.__defaults__ != None and \
            k >= nbArg-nbDef:
                dft = f.__defaults__[k-nbArg+nbDef]
            URI += "/{" + var + "}"
            app.get(URI,  tags=[tag])(f)

#
# eventually add the index feature
#
def guide():
    page = '''<html><head><title>{}</title></head><body>{}</body></html>'''
    page = page.format(title, features.__doc__.replace("\n","<br>"))
    page = page.replace("/docs", '<a href="/docs">/docs</a>')
    page = page.replace("clovis.corde@imerir.com",'<a href="mailto:clovis.corde@imerir.com">clovis.corde@imerir.com</a>')
    return page
app.get("/", response_class=HTMLResponse)(guide)
app.get("/index", response_class=HTMLResponse)(guide)