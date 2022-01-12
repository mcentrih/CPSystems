using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Logic : MonoBehaviour
{
    public List<GameObject> gameObjectsRandomPos;
    public List<GameObject> gameObjectsFinalPos;
    public List<GameObject> objectsToSpawn;

    private List<GameObject> onSceneObjs;

    private int pos = 0;

    bool foundEveryObject = false;

    // Start is called before the first frame update
    void Start()
    {
        onSceneObjs = new List<GameObject>();

        List<int> indexes = GenerateRandomList();
        for (int i = 0; i < objectsToSpawn.Count; i++)
        {
            onSceneObjs.Add(Instantiate(objectsToSpawn[i], gameObjectsRandomPos[indexes[i]].transform.position, objectsToSpawn[i].transform.rotation));
        }
    }

    private void Update()
    {
        if (!foundEveryObject)
        {
            // iskanje objektov
            if (Input.touchCount > 0 && Input.touches[0].phase == TouchPhase.Began)
            {
                Ray ray = Camera.current.ScreenPointToRay(Input.GetTouch(0).position);
                RaycastHit hit;
                if (Physics.Raycast(ray, out hit))
                {
                    Debug.Log("Found: " + hit.collider.name + "\nCompare to: " + objectsToSpawn[pos].transform.name);
                    if (hit.collider.name == onSceneObjs[pos].transform.name)
                    {
                        onSceneObjs[pos].transform.position = gameObjectsFinalPos[pos].transform.position;
                       // onSceneObjs[pos].transform.rotation = gameObjectsFinalPos[pos].transform.rotation;

                        pos++;

                        if (pos > 5) foundEveryObject = true;
                    }
                    else
                    {
                        ResetScene();
                    }
                }
            }
        }
    }

    private void ResetScene()
    {
        List<int> indexes = GenerateRandomList();

        for (int i = 0; i < 6; i++)
        {
            onSceneObjs[i].transform.position = gameObjectsRandomPos[indexes[i]].transform.position;
        }

        pos = 0;
    }

    public List<int> GenerateRandomList()
    {
        List<int> list = new List<int>();
        for (int i = 0; i < 6; i++)
        {
            int numToAdd = Random.Range(0, 6);
            while (list.Contains(numToAdd))
            {
                numToAdd = Random.Range(0, 6);
            }
            list.Add(numToAdd);
        }

        return list;
    }
}